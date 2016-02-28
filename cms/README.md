Install CMS modules and example content
=========================================

There are a number of CMS packages that can be installed in SDL Tridion 2013 SP1/SDL Web 8.
The following packages are provided:

* ECommerceModules_v1.0.0.zip - Contains for E-Commerce and Navigation modules + modified version of the HTML white label design
* ECommerceContentStructure_v1.0.0.zip - E-Commerce content types and page types, base content structure and standard pages 
* FredhopperExampleContent_v1.0.0.zip - Example content for Fredhopper (optional)
* HybrisExampleContent_v1.0.0.zip - Example content for Hybris (optional)
* DemandwareExampleContent_v1.0.0.zip - Example content for Demandware (optional)

Instructions
-------------

The CMS import script is generic and is used for all packages. The syntax for calling the script:

```.\cms-import.ps1  -cmsUrl [CMS url] -moduleZip [Module ZIP filename]
```

To setup CMS data the packages needs to be imported in the following order:

1. Setup modules: `.\cms-import.ps1 -moduleZip ECommerceModules_v1.0.0.zip`
2. Setup content structure: `.\cms-import.ps1 -moduleZip ECommerceContentStructure_v1.0.0.zip`
3. Optional setup example content for the current E-Commerce system:
    - Fredhopper: `.\cms-import.ps1 -moduleZip FredhopperExampleContent_v1.0.0.zip`
    - Hybris: `.\cms-import.ps1 -moduleZip HybrisExampleContent_v1.0.0.zip`
    - Demandware: `.\cms-import.ps1 -moduleZip DemandwareExampleContent_v1.0.0.zip`
    


